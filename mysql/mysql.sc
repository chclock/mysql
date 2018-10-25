(library (mysql mysql)
    (export
        ;; const
        mysql-stmt-init-done
        mysql-stmt-prepare-done
        mysql-stmt-execute-done
        mysql-stmt-fetch-done

        ;; ftype
        mysql-field
        mysql-row
        mysql-field-offset
        mysql-rows
        mysql-row-offset
        mysql
        mysql-res
        mysql-parameters
        mysql-field-offset
        
        ;; ffi
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

        ;; syntax
        mysql:connect
        mysql:query
        mysql:error
        )
    (import 
        (scheme)
        (mysql const)
        (mysql ftype)
        (mysql ffi)
        (mysql syntax)
        )
)
