#include "io.h"

int a;
char c;

int foo(int foo) {
  int a;
  {
    int n;
    int c;
    return 9;
  }
}

void main() {
  {
    foo(a);
    {
      char foo;
      foo = 'd';
      
    }
  }
}
