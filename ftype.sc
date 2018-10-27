(library (mysql ftype)
    (export
        mysql-field
        mysql-row
        mysql-field-offset
        mysql-rows
        mysql-row-offset
        mysql
        mysql-res
        mysql-parameters
        )
        (import (scheme))

    (define-ftype mysql-field
        (struct
            (name (* char))                    ;     Name of column 
            (org_name (* char))                ;     Original column name, if an alias 
            (table (* char))                   ;     Org table name, if table was an alias 
            (org-table (* char))               ;     Database for table 
            (db (* char))                      ;     Catalog for table 
            (catalog (* char))                 ;     Default value (set by mysql_list_fields) 
            (def (* char))                     ;     Width of column (create length) 
            (length unsigned-long)             ;     Max width for selected set 
            (max-length unsigned-long)
            (name-length unsigned-int)
            (org-name-length unsigned-int)
            (table-length unsigned-int)
            (org-table-length unsigned-int)
            (db-length unsigned-int)
            (catalog-length unsigned-int)
            (def-length unsigned-int)
            (flags unsigned-int)                ;     Div flags 
            (decimals unsigned-int)             ;     Number of decimals in field 
            (charsetnr unsigned-int)            ;     Character set 
            (type unsigned-8)                   ;     Type of field. See mysql_com.h for types 
            (extension void*)))

    (define-ftype mysql-row (* char))           ;     return data as array of strings 

    (define-ftype mysql-field-offset unsigned-int)    ;  offset to current field 

    (define-ftype mysql-rows
        (struct
            (next (* mysql-rows))
            (data (* mysql-row))
            (length unsigned-long)))

    (define-ftype mysql-row-offset (* mysql-rows))

    (define-ftype mysql (struct))

    (define-ftype mysql-res (struct))

    (define-ftype mysql-parameters
        (struct
            (p-max-allowed-packet (* unsigned-long))
            (p-net-bufffer-length (* unsigned-long))
            (extension void*)))
)
