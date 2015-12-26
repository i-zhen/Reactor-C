#include "io.h"
int h;
char h2;

int foo(int a, int b) {
    return a + b;
}
void main(){
    int a;
    int b;
    h = read_i();
    a = 99;
    b = foo(a,h);
    print_i(b);
}
