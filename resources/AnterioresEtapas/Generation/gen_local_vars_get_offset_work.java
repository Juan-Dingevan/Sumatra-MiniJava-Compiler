///[SinErrores]
class A {
    void m1() {}

    void m2(int x) {
        var zeroDepthBlockVar = 1;
        if(3 > 2) {
            var oneDepthBlockVar_IF = 1;
        } else {
            var oneDepthBlockVar_ELSE = 2;
        }

        while(2 > 3) {
            var oneDepthBlockVar_WHILE = 1;
            if(oneDepthBlockVar_WHILE > 2) {
                var twoDepthBlockVar = 1;
            }
        }

        {
            {
                {
                    var threeDepthBlockVar = 1;
                }
            }
        }

    }

    void m3(int x, int y) {}
}

class Main {
    public static void main() {}
}