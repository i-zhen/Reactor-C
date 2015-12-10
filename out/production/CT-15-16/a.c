#include "io.h"

int a;

int foo(int n) {
  int a;
  {
    int n;
    n = 1;
    return 0;
  }
}

void main() {
  {
    foo(a,a);
    {
      a = 0 * foo(a);
    }
  }
}
