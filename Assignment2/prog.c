/*
 * Name: Saja Elkurtehi
 * Student ID: 300288667
 */

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

int a, b, turn;
pthread_mutex_t lock;

void *th0(void *arg)
{
    for (int i = 0; i < a; i++)
    {
        while (turn != 0)
            ;
        pthread_mutex_lock(&lock);
        b += 1;
        printf("Thr0, (b+1=%d)\n", b);
        turn = 1;
        pthread_mutex_unlock(&lock);
    }
    return NULL;
}

void *th1(void *arg)
{
    for (int i = 0; i < a; i++)
    {
        while (turn != 1)
            ;
        pthread_mutex_lock(&lock);
        b += 2;
        printf("Thr1, (b+2=%d)\n", b);
        turn = 2;
        pthread_mutex_unlock(&lock);
    }
    return NULL;
}

void *th2(void *arg)
{
    for (int i = 0; i < a; i++)
    {
        while (turn != 2)
            ;
        pthread_mutex_lock(&lock);
        b += 3;
        printf("Thr2, (b+3=%d)\n", b);
        turn = 3;
        pthread_mutex_unlock(&lock);
    }
    return NULL;
}

void *th3(void *arg)
{
    for (int i = 0; i < a; i++)
    {
        while (turn != 3)
            ;
        pthread_mutex_lock(&lock);
        b += 4;
        printf("Thr3, (b+4=%d)\n", b);
        turn = 0;
        pthread_mutex_unlock(&lock);
    }
    return NULL;
}

void print_fibonacci(int n)
{
    int v1 = 1, v2 = 1, v3;
    if (n <= 1)
    {
        printf("%d ", v1);
    }
    else
    {
        printf("%d %d ", v1, v2);
        for (int i = 2; i < n; i++)
        {
            v3 = v1 + v2;
            printf("%d ", v3);
            v1 = v2;
            v2 = v3;
        }
    }
    printf("\n");
}

int main()
{
    printf("Enter integer a value: ");
    scanf("%d", &a);
    printf("Enter integer b value: ");
    scanf("%d", &b);
    printf("Enter the Thread # to start first (0 to 3): ");
    scanf("%d", &turn);

    pthread_t threads[4];
    pthread_mutex_init(&lock, NULL);

    pthread_create(&threads[0], NULL, th0, NULL);
    pthread_create(&threads[1], NULL, th1, NULL);
    pthread_create(&threads[2], NULL, th2, NULL);
    pthread_create(&threads[3], NULL, th3, NULL);

    for (int i = 0; i < 4; i++)
    {
        pthread_join(threads[i], NULL);
    }

    printf("Parent, (b=%d)\n", b);
    printf("The Fibonacci sequence for %d is:\n", b);
    print_fibonacci(b);

    pthread_mutex_destroy(&lock);
    return 0;
}
