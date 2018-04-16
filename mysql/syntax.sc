(library (mysql syntax)
  (export
    mysql:connect
    mysql:query
    mysql:error
    )
  (import 
    (scheme)
    (mysql const)
    (mysql ftype)
    (mysql ffi)
    )
      
  (define-syntax make-ftype-null
    (syntax-rules ()
      ((_ type)
        (make-ftype-pointer type #x00000000))))

  (define mysql:connect
    (case-lambda
      ([host user passwd db] 
        (mysql:connect host user passwd db 3306))
      ([host user passwd db port]
        (mysql:connect host user passwd db 3306 #f 0))
      ([host user passwd db port unix_socket clientflag]
        (let* ([conn (mysql-init (make-ftype-null mysql))]
              [rst (mysql-real-connect conn host user passwd db port unix_socket clientflag)])
          (if (ftype-pointer-null? rst)
            (mysql:error conn "connect")
            conn)))))

  (define mysql:query
    (case-lambda
      ((conn query)
        (mysql:query conn query 'list))
      ((conn query format)
      (if (zero? (mysql-query conn query))
        (let ([result (mysql-store-result conn)])
          (if (not (ftype-pointer-null? result))
            (let* ([num-fields (mysql-num-fields result)]
                   [fields (parser-fields result num-fields)]
                   [rows (parser-rows result fields '())])
              (mysql-free-result result)
              (case format
                ('list rows)
                ('json (mysql-list->json rows fields))
                (else rows)))))
        (mysql:error conn "query")
      ))))

  (define parser-fields
    (lambda (result num-fields)
      (mysql-field-seek result 0)
      (let ([fields (make-vector num-fields)])
        (let loop ([idx 0])
          (let ([field (mysql-fetch-field result)])
            (if (ftype-pointer-null? field)
              fields
              (begin
                (vector-set! fields idx (cons 
                                          (field-name field)
                                          (field-type field)))
                (loop (+ idx 1))
              )))))))

  (define parser-rows
    (lambda (result fields rows)
      (let ([row (mysql-fetch-row result)])
        (if (ftype-pointer-null? row)
          (reverse rows)
          (parser-rows result fields (cons (parser-columns row fields) rows))
        ))))

  (define parser-columns
    (lambda (row fields)
      (let* ([len (vector-length fields)]
             [vec (make-vector len)])
        (let loop ([idx 0])
          (if (= idx len)
            vec
            (let ([type (cdr (vector-ref fields idx))])
              (vector-set! vec idx (parser-cell row idx type))
              (loop (+ idx 1))
            ))))))

  (define parser-cell
    (lambda (row column-index type)
      (if (null-cell? row column-index)
        'null
        (type-convert (cell-string row column-index) type)
      )))

  (define null-cell?
    (lambda (row column-index)
      (eq? 0 (foreign-ref 'unsigned-8
                          (ftype-pointer-address
                            (ftype-&ref mysql-row () row column-index))
                          0))))

  (define cell-string
    (lambda (row column-index)
      (char*->string
        (ftype-pointer-address
          (ftype-&ref mysql-row (*) row column-index)))))

  (define field-name
    (lambda (field)
      (char*->string 
        (ftype-pointer-address
          (ftype-&ref mysql-field (name 0) field 0)))))

  (define field-type-map
    (let ([table (make-eq-hashtable)])
      (map 
        (lambda (pair)
          (hashtable-set! table (car pair) (cdr pair)))
        '((0 . decimal)
          (1 . tiny)
          (2 . short)
          (3 . long)
          (4 . float)
          (5 . double)
          (6 . null)
          (7 . timestamp)
          (8 . longlong)
          (9 . int24)
          (10 . date)
          (11 . time)
          (12 . datetime)
          (13 . year)
          (14 . newdate)
          (15 . varchar)
          (16 . bit)
          (17 . timestamp2)
          (18 . datetime2)
          (19 . time2)
          (245 . json)
          (246 . newdecimal)
          (247 . enum)
          (248 . set)
          (249 . tiny-blob)
          (250 . medium-blob)
          (251 . long-blob)
          (252 . blob)
          (253 . var-string)
          (254 . string)
          (255 . geometry)))
      table))
  
  (define field-type-symbol
    (lambda (field-type)
      (hashtable-ref field-type-map field-type '())))
    
  (define field-type
    (lambda (field)
      (field-type-symbol
        (ftype-ref mysql-field (type) field 0))))
        
  (define char*->string
    (lambda (address)
      (list->string
        (reverse
          (let loop ((pointer address) (chars '()))
            (let ((char (foreign-ref 'char pointer 0)))
              (if (char=? char #\nul)
                  chars
                  (loop (+ 1 pointer) (cons char chars)))))))))

  (define type-convert
    (lambda (value type)
      (case type
        ((decimal tiny short long float double longlong int24 newdecimal)
          (string->number value))
        ((datetime timestamp)
          (date-string->date value))
        (else value))))

  (define date-string->date
    (lambda (date-string)
      (let ((part (lambda (start end)
                    (string->number
                      (substring date-string start end)))))
        (let ((year (part 0 4))
              (month (part 5 7))
              (day (part 8 10))
              (hour (part 11 13))
              (minute (part 14 16))
              (second (part 17 19)))
          (make-date 0 second minute hour day month year 0)))))

  (define mysql-list->json
    (lambda (rows fields)
      (let* ([len (length rows)]
             [vec (make-vector len)]
             [field-len (vector-length fields)])
        (let loop1 ([idx1 0])
          (if (< idx1 len)
            (let loop2 ([idx2 0]
                        [lst '()])
              (if (< idx2 field-len)
                (loop2 (+ 1 idx2)
                      (cons 
                        (cons
                          (car (vector-ref fields idx2))
                          (vector-ref (list-ref rows idx1) idx2))
                        lst))
                (begin
                  (vector-set! vec idx1 (reverse lst))
                  (loop1 (+ 1 idx1)))))
            vec
          )))))

  (define mysql:error
    (lambda (conn name)
      (let ([msg (format "errno: ~a, error: ~a" (mysql-errno conn) (mysql-error conn))]
            [act (string-append "mysql:" name)])
        (mysql-close conn)
        (error act msg))))
)