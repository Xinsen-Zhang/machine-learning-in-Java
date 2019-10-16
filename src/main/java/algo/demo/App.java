package algo.demo;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        System.out.println("launch the process of generatingNDArray on gpu");
        INDArray matrix = Nd4j.rand(10000, 10000);
        System.out.println("launch the process of matrix multiplication");
        INDArray result = matrix;
        long startTime = System.currentTimeMillis();
        long endTime = 0l;
        for (int i = 0; i < 10000; i++) {
            result = result.mmul(matrix);
            endTime = System.currentTimeMillis();
            if (i % 100 == 99) {
                System.out.println(String.format("multiplication over 100 times, it cost %d ms", endTime - startTime));
                startTime = System.currentTimeMillis();
            }
        }
    }
}
