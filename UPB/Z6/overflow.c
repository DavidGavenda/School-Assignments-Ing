#include <stdio.h>
#include <string.h>

int main(void)
{
    char buffer[10];
    int succ = 0;
    printf("Zadaj heslo\n");
    gets(buffer);

    if(strcmp(buffer,"spravneHeslo"))
    {
        printf("ZLE HESLO");
    }
    else 
    {
        printf("SPRAVNE HESLO");
        succ = 1;
    }

    if(succ)
    {
        printf("\nUspesne prihlasenie");
    }
    return 0;
}