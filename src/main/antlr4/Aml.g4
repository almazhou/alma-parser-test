grammar Aml;

file : (data)+
;

data : keyword = ('puid' | 'rule' | 'group') data_name = (INT | TEXT) ('extends' extend_name = ID)? '{'
        (property ';')+
'}'
;

property : property_name = ID eq_sign property_value = value
;

value : (ID(comma ID)* | INT (comma INT)* | STRING | TEXT(comma TEXT)*)
;

comma : ','
;

eq_sign : '='
;

ID : [A-Za-z]+
;
INT : [0-9]+
;

LINE_COMMENT : '//' .*? '\r'? '\n' -> channel(HIDDEN)
; 

COMMENT : '/*' .*? '*/' -> channel(HIDDEN)
; 

WS : [ \n\r\t]+ -> skip 
;

STRING: '"' .*? '"';

TEXT : ~[ "\n\r\t;,]+ 
;
