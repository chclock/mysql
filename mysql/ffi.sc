(library (mysql ffi)
    (export
        mysql-lib-load
        mysql-init
        mysql-real-connect
        mysql-query
        mysql-store-result
        mysql-use-result
        mysql-fetch-field
        mysql-field-seek
        mysql-fetch-row
        mysql-free-result
        mysql-num-rows
        mysql-num-fields
        mysql-errno
        mysql-error
        mysql-get-client-info
        mysql-close
        )
    (import 
        (scheme)
        (mysql const)
        (mysql ftype)
        )
            
    (define lib-name
        (case (machine-type)
            ((i3nt ti3nt a6nt ta6nt) "C:\\Program Files\\MySQL\\MySQL Connector C 6.1\\lib\\libmysql.dll")
            ((a6osx i3osx ta6osx ti3osx) "libmysqlclient.dylib")
            ((a6le i3le ta6le ti3le) "libmysqlclient.so")
            (else "libmysqlclient.so")))

    (define mysql-lib-load
        (lambda (path)
            (load-shared-object path)))

    (define init (mysql-lib-load lib-name))

    (define-syntax define-procedure
        (syntax-rules ()
            ((_ name sym args rst)
                (define name
                    (if (foreign-entry? sym)
                        (foreign-procedure sym args rst)
                        (lambda x (printf "error: ~a not found in ~a\n" sym lib-name)))))))

    (define-procedure mysql-init "mysql_init" ((* mysql)) (* mysql))

    (define-procedure mysql-real-connect "mysql_real_connect" ((* mysql) string string string string unsigned-int string unsigned-long) (* mysql))

    (define-procedure mysql-query "mysql_query" ((* mysql) string) int)

    (define-procedure mysql-store-result "mysql_store_result" ((* mysql)) (* mysql-res))
    
    (define-procedure mysql-use-result "mysql_use_result" ((* mysql)) (* mysql-res))

    (define-procedure mysql-fetch-field "mysql_fetch_field" ((* mysql-res)) (* mysql-field))

    (define-procedure mysql-field-seek "mysql_field_seek" ((* mysql-res) unsigned-int) unsigned-int)

    (define-procedure mysql-fetch-row "mysql_fetch_row" ((* mysql-res)) (* mysql-row))

    (define-procedure mysql-free-result "mysql_free_result" ((* mysql-res)) void)
    
    (define-procedure mysql-num-rows "mysql_num_rows" ((* mysql-res)) unsigned-long-long)

    (define-procedure mysql-num-fields "mysql_num_fields" ((* mysql-res)) unsigned-int)

    (define-procedure mysql-errno "mysql_errno" ((* mysql)) unsigned-int)
    
    (define-procedure mysql-error "mysql_error" ((* mysql)) string)

    (define-procedure mysql-get-client-info "mysql_get_client_info" () string)

    (define-procedure mysql-close "mysql_close" ((* mysql)) void)
)
