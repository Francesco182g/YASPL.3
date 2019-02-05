#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

int op1, op2;
int res;
int op;

void
somma (int op1, int op2, int *res)
{

  *res = op1 + op2;

}

void
sub (int op1, int op2, int *res)
{

  *res = op1 - op2;

}

void
mul (int op1, int op2, int *res)
{

  *res = op1 * op2;

}

void
mulSom (int op1, int op2, int *res)
{
  double i = op1;
  int risultato = 0;
  if (((op2 == 0) || (op1 == 0)))
    {
      risultato = 0;
    }
  while ((i < op2))
    {

      risultato = risultato + op1;

      i = i + 1;
    }


  *res = risultato;

}

void
divi (int op1, int op2, int *res)
{

  *res = op1 / op2;

}

void
elpot (int op1, int op2, int *res)
{
  int i = 0;
  int risu = 1;
  if ((op2 == 0))
    {
      *res = 0;
    }
  if ((op2 == 1))
    {
      *res = op1;
    }
  if ((op2 > 1))
    {
      while (!((i == op2)))
	{

	  risu = risu * op1;

	  i = i + 1;
	}

    }

  *res = risu;

}

void
fibit (int n, int *res)
{
  int i = 1;
  int risultato = 1;
  int x = 0, y = 1, z = 1;
  if ((n == 0))
    {
      risultato = 0;
    }
  if ((n == 1))
    {
      risultato = 1;
    }
  else
    {
      x = n;
      while (!((i == n)))
	{

	  x = y;

	  y = z;

	  z = x + y;

	  risultato = z;

	  i = i + 1;
	}

    }

  *res = risultato;

}

main ()
{

  printf
    ("Inserisici il tipo di operazione: 1:Add 2:Sub 3:Mult 4:Div 5:Pot 6:Fib \n");
  scanf ("%d", &op);
  if ((op == 1))
    {
      printf ("Inserisci il primo operatore e il secondo: \n");
      scanf ("%d", &op1);
      scanf ("%d", &op2);

      somma (op1, op2, &res);
    }
  if ((op == 2))
    {
      printf ("Inserisci il primo operatore e il secondo: \n");
      scanf ("%d", &op1);
      scanf ("%d", &op2);

      sub (op1, op2, &res);
    }
  if ((op == 3))
    {
      printf ("Inserisci il primo operatore e il secondo: \n");
      scanf ("%d", &op1);
      scanf ("%d", &op2);

      mulSom (op1, op2, &res);
    }
  if ((op == 4))
    {
      printf ("Inserisci il primo operatore e il secondo: \n");
      scanf ("%d", &op1);
      scanf ("%d", &op2);

      divi (op1, op2, &res);
    }
  if ((op == 5))
    {
      printf ("Inserisci il primo operatore e il secondo: \n");
      scanf ("%d", &op1);
      scanf ("%d", &op2);

      elpot (op1, op2, &res);
    }
  if ((op == 6))
    {
      printf ("Inserisci il valore n di fibonacci: \n");
      scanf ("%d", &op1);

      fibit (op1, &res);
    }

  printf ("Stampo il risultato: \n" "%d\n", res);

  printf ("Ciao\n");

  system ("pause");

  return 0;
}
