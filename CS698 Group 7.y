%{
#include<stdio.h>
#include<stdlib.h>

FILE * fp;

int flag = 0, cflag = 0, pflag = 0, i = 1, j = 0;
int p = -1, q = 0, k = 0, n = 0;

long num;

char *ptr;
char *values[35][15];
int yylex(void);
%}

%union{
 char c;
 char s[5000];
}

%token <s> TAG
%token <s> TEXT
%token <s> QDIV
%token <s> LF
%token <s> CALL
%token <s> HREF
%token <s> CTD
%token <s> PTD
%token <s> PUT
%token <s> SPAN
%token <s> CHTML
%token <c> OA
%token <c> CA

%%


file:
    | file tags LF

tags:
    | CHTML tags	{ fprintf(fp, "-----HTML-----\n\n");
    				k++;
    				p = -1;
    			}
    | TEXT tags	{
			if(j == 1){
				i = 1;
				j = 0;
				values[p][q] = $1;
				fprintf(fp, "%s\n", values[p][q]);
			}
		}
    | TAG tags	{
    			if(j == 1){
				j = 0;
				values[p][q] = "N/A";
				fprintf(fp, "%s\n", values[p][q]);
    			}
		}
    | QDIV TEXT tags { fprintf(fp, "Stock Price: %s\n\n", $2); }
    | CALL TEXT tags	{
    				p++;
				q = 0;
    				fprintf(fp, "%s CALL ", $2); 
				printf("%s CALL ", $2);
			}
    | CALL TAG tags
    | PUT TEXT tags	{ fprintf(fp, "PUT "); 
    				printf("PUT ");
    			} 
    | PUT TAG tags
    | CTD TEXT tags	{	
    				if(q == 7){
					cflag = 0;
					values[p][q] = $2;
					fprintf(fp, "%s ", values[p][q]);
				} else{
					values[p][q] = $2;
					fprintf(fp, "%s ", values[p][q]);
				}
				q++;
			}
    | CTD TAG tags	{
					values[p][q] = "N/A";
					fprintf(fp, "%s ", values[p][q]);
					q++;
			}
    | PTD TEXT tags	{
    				if(q < 13){
					values[p][q] = $2;
					fprintf(fp, "%s ", values[p][q]);
					q++;
				}
			}
    | PTD TAG tags	{
    				if(q < 13){
					values[p][q] = "N/A";
					fprintf(fp, "%s ", values[p][q]);
					q++;
				}
			}
    | PTD SPAN tags	
%%

#include <ctype.h>


int yylex(void)
{
	int c, i = 0;
	char *v = yylval.s;
	c = getc(stdin);
	if(c == EOF) 
		return 0;
	switch(c)
	{
		case '\n': //printf("%c", c); 
			   return LF;
		case '\r': //printf("%c", c); 
			   return LF;
		case '<' : flag = 1; 
	}
	if(flag == 0)
	{
		ungetc(c, stdin);
		scanf("%[^<]", v);
		//printf("%s\n", v);
		return TEXT;
	}
	else
	{
		flag = 0;
		ungetc(c, stdin);
		scanf("%[^>]", v);
		c = getc(stdin);
		//printf("%s%c\n", v, c);
		if(strstr(v, "qwidget_lastsale"))
			return QDIV;
		else if(strstr(v, "jpm-call")){
			cflag = 1;
			return CALL;
		}
		else if(strstr(v, "jpm-put")){
			pflag = 1;
			return PUT;
		}
		else if(!strstr(v, "/td") && strstr(v, "td") && cflag == 1)
			return CTD;
		else if(!strstr(v, "/td") && strstr(v, "td") && pflag == 1)
			return PTD;
		else if(strstr(v, "/tr") && pflag == 1)
			pflag = 0;
		else if(strstr(v, "/html"))
			return CHTML;
		else if(!strstr(v, "/span") && strstr(v, "span") && pflag == 1){
			j = 1;
			return SPAN;
		}
		return TAG;
	}
}

int yyerror(char *s){printf("yyerror: %s\n", s);}
int main(int argc, char **agrv)
{
	fp = fopen("result.csv", "w+");
	if(fp == NULL)
	{
		printf("Couldn't open file\n");
		return 1;
}
	yydebug=1;
	yyparse();
}
