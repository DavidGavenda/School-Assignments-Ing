#include <stdio.h>
#include <stdlib.h>
 
enum { BUFFER_SIZE = 10 };
 
int main() {
    char buffer[BUFFER_SIZE];
 
    int length = snprintf(buffer, BUFFER_SIZE, "%s%s", "long-name", "suffix");
 
    if (length >= BUFFER_SIZE) {
        printf("Uspesne ochranene");
    }
 
    return EXIT_SUCCESS;
}