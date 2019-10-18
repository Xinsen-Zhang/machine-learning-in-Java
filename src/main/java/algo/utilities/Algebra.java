package algo.utilities;

public class Algebra {

    public static double innerProduct(double[] vector1, double[] vector2) throws Exception {
        if (vector1.length != vector2.length) {
            throw new Exception("向量长度不一致");
        }
        int length = vector1.length;
        double product = 0.0;
        for (int i = 0; i < length; i++) {
            product += (vector1[i] * vector2[i]);
        }
        return product;
    }

    public Vector householderTransform(Matrix matrix, int dimension) throws Exception {
        double[][] data = matrix.getData();
        int length = matrix.getRowCount();
        double[] vectorData = new double[length];
        if (dimension > matrix.getColCount()) {
            throw new Exception("dimension 维度超过指标");
        }
        for (int i = 0; i < length; i++) {
            vectorData[i] = data[i][dimension];
        }
        Vector x = new Vector(vectorData);
        double norm =x.getVectorL2Norm();
        x.getData()[dimension] -= norm;
        norm = x.getVectorL2Norm();
        x.divide(norm);
        return x;
    }

    public static void QR(Matrix A, Matrix Q, Matrix R) {
        // TODO QR 分解
    }
}
