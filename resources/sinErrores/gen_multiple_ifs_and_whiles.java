///0-1-2-3-4-5-6-7-8-9&0-1-2-3-4-5-6-7-8&0-1-2-3-4-5-6-7&0-1-2-3-4-5-6&0-1-2-3-4-5&0-1-2-3-4&0-1-2-3&0-1-2&0-1&0&exitosamente
class Main {
    private static int x;
    private static int y;
    public static void main() {
        x = 10;
        y = 0;
        var outerLoop = true;
        var innerLoop = true;

        while(outerLoop) {
            while(innerLoop) {
                System.printI(y);

                y = y + 1;
                innerLoop = y != x;

                if(innerLoop)
                    System.printS("-");
            }

            System.printSln("");

            x = x - 1;
            y = 0;

            innerLoop = true;
            outerLoop = x > 0;
        }
    }
}