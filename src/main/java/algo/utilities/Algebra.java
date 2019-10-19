package algo.utilities;

public class Algebra {

    /**
     * 计算两个向量的内积
     * @param vector1
     * @param vector2
     * @return 内积
     * @throws Exception
     */
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

    public static double dot(double[] vector1, double [] vector2) throws Exception {
        return innerProduct(vector1, vector2);
    }

    public static double[][] dot(double[][] matrix1, double[][] matrix2) throws Exception {
        return mmul(matrix1, matrix2);
    }

    /**
     * 计算向量的l2范数
     * @param vector
     * @return 范数
     */
    public static double getL2Norm(double[] vector) {
        double norm = 0.0;
        int length = vector.length;
        for (int i = 0; i < length; i++) {
            norm += (vector[i] * vector[i]);
        }
        return Math.sqrt(norm);
    }

    public static int getRowCount(double[][] matrix) {
        return matrix.length;
    }

    public static int getColCount(double[][] matrix) {
        int colCount = 0;
        if (getRowCount(matrix) > 0) {
            colCount = matrix[0].length;
        }
        return colCount;
    }

    public static double[][] transpose(double[][] matrix) {
        int m = getRowCount(matrix);
        int n = getColCount(matrix);
        if (n == 0) {
            return null;
        }
        double[][] matrix2 = new double[n][m];
        for (int i = 0 ; i < m; i++) {
            for (int j = 0; j < n; j++) {
                matrix2[j][i] = matrix[i][j];
            }
        }
        return matrix;
    }

    public static double[][] mmul(double[][] matrix1, double[][] matrix2) throws Exception {
        if (getColCount(matrix1) != getRowCount(matrix2) || getRowCount(matrix2) == 0) {
            throw new Exception("please check the shape of matrix1 and matrix2");
        }
        int m = getRowCount(matrix1);
        int n = getColCount(matrix2);
        int p = getColCount(matrix1);
        double[][] result = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = 0.0;
                for (int k = 0; k < p; k++) {
                    result[i][j] += (matrix1[i][k] * matrix2[k][j]);
                }
            }
        }
        return result;
    }

    public static double[][] createZeros(int m, int n) {
        double[][] matrix = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n;j++) {
                matrix[i][j] = 0.0;
            }
        }
        return matrix;
    }

    public static double[][] createZeros(int m) {
        return createZeros(m, m);
    }

    public static double[][] createOnes(int m, int n) {
        double[][] matrix = createZeros(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = 1.0;
            }
        }
        return matrix;
    }

    public static double[][] createOnes(int m) {
        return createOnes(m, m);
    }

    public static double[][] createRandoms(int m, int n) {
        double[][] matrix = createZeros(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = Math.random();
            }
        }
        return matrix;
    }

    public static double[][] createRandoms(int m) {
        return createRandoms(m, m);
    }

    public static void print(double[] vector, int axis) {
        int length = vector.length;
        if (axis == 0) {
            for (int i = 0; i < length; i++) {
                System.out.println(vector[i]);
            }
        } else if (axis == 1) {
            String[] temp = new String[length];
            for (int i = 0; i < length; i++) {
                temp[i] = String.valueOf(vector[i]);
            }
            System.out.println(String.join("\t", temp));
            temp = null;
        }
    }

    public static void print(double[][] matrix) {
        int nRows = getRowCount(matrix);
        for (int i = 0; i < nRows; i++) {
            print(matrix[i], 1);
        }
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
