# mysql

mySQL binding for Chez Scheme

1. define mysql database set

`(define conn (connect "IP" "Count" "Password" "DatabaseName"))`

2. execute query

`(query conn "SELECT * FROM TableName")`

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
