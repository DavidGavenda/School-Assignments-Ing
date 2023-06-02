#include <stdio.h>
#include <stdlib.h>
 
int main() {
    char buffer[10];
    int check = 0;
 
    sprintf(buffer, "%s", "This string is too long!");
 
    printf("check: %d", check); /* Malo vypisat 0, nespravi tak*/
 
    return EXIT_SUCCESS;
}