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
  (lambda (conn query)
    (if (zero? (mysql-query conn query))
      (let* ([res (mysql-store-result conn)]
             [num (mysql-num-fields res)])
        (printf "~a\n" (number? num))
        (printf "~a\n" (string? num))
        
        (eval (mysql:fetch-row res 4))
      )
      (mysql:error conn "query")
    )))

(define-syntax mysql:fetch-row
  (syntax-rules ()
    ((_ res num)
     (begin
        (define-ftype cur-row (array ,@num (* char)))
        (let ([fetch-cur-row (foreign-procedure "mysql_fetch_row" ((* mysql-res)) (* cur-row))])
          (let loop ([row (fetch-cur-row res)])
            (unless (ftype-pointer-null? row)
              (printf "row : ~a\n" (ftype-pointer->sexpr row))
              (loop (fetch-cur-row res)))))))))

              
(define mysql:error
  (lambda (conn name)
    (let ([msg (format "errno: ~a, error: ~a" (mysql-errno conn) (mysql-error conn))]
          [act (string-append "mysql:" name)])
      (mysql-close conn)
      (error act msg))))