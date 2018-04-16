# mysql

mySQL binding for Chez Scheme

1. define mysql connection

> (define conn (mysql:connect "localhost" "root" "123456" "sys"))

2. execute sql

> (mysql:query conn "SELECT * FROM sys.sys_config")  
> or  
> (mysql:query conn "SELECT * FROM sys.sys_config" 'list)

=>

```
(
    #(diagnostics.allow_i_s_tables OFF #<date Sat Mar  3 00:33:37 2018> null) 
    #(diagnostics.include_raw OFF #<date Sat Mar  3 00:33:37 2018> null) 
    #(ps_thread_trx_info.max_length 65535 #<date Sat Mar  3 00:33:37 2018> null) 
    #(statement_performance_analyzer.limit 100 #<date Sat Mar  3 00:33:37 2018> null) 
    #(statement_performance_analyzer.view null #<date Sat Mar  3 00:33:37 2018> null) 
    #(statement_truncate_len 64 #<date Sat Mar  3 00:33:37 2018> null)
)
```

or 

> (mysql:query conn "SELECT * FROM sys.sys_config" 'json)

=>

```
#(
    ((variable . diagnostics.allow_i_s_tables) (value . OFF) 
        (set_time . #<date Sat Mar  3 00:33:37 2018>) (set_by . null))
    ((variable . diagnostics.include_raw) (value . OFF) 
        (set_time . #<date Sat Mar  3 00:33:37 2018>) (set_by . null)) 
    ((variable . ps_thread_trx_info.max_length) (value . 65535) 
        (set_time . #<date Sat Mar  3 00:33:37 2018>) (set_by . null)) 
    ((variable . statement_performance_analyzer.limit) (value . 100)
        (set_time . #<date Sat Mar  3 00:33:37 2018>) (set_by . null)) 
    ((variable . statement_performance_analyzer.view) (value . null)
        (set_time . #<date Sat Mar  3 00:33:37 2018>) (set_by . null)) 
    ((variable . statement_truncate_len) (value . 64) 
        (set_time . #<date Sat Mar  3 00:33:37 2018>) (set_by . null))
)
```

you can use json library to convert this vector to json string