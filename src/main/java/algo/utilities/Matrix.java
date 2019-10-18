package algo.utilities;

public class Matrix {
    private double[][] data;
    public Matrix(double[][] data) {
        this.data = data;
    }

    public Matrix(Matrix matrix) {
        this.data = matrix.getData();
    }

    public double[][] getData() {
        return data;
    }

    public static Matrix createIndentityMatrix(int size) {
        double[][] data = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    data[i][j] = 1.0;
                } else {
                    data[i][j] = 0.0;
                }
            }
        }
        return new Matrix(data);
    }

    public static Matrix createZeros(int size) {
        double[][] data = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                data[i][j] = 0.0;
            }
        }
        return new Matrix(data);
    }

    public int getRowCount() {
        return data.length;
    }

    public int getColCount() {
        return data[0].length;
    }

    public Matrix mmul(Matrix matrix) throws Exception {
        if (getColCount() != matrix.getRowCount()) {
            throw new Exception("请检查矩阵相乘的形状");
        }
        double[][] data2 = matrix.getData();
        int m = getRowCount();
        int n = matrix.getColCount();
        double[][] result = new double[m][n];
        int p = getColCount();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = 0.0;
                for (int k = 0; k < p; k++) {
                    result[i][j] += (data[i][k] * data2[k][j]);
                }
            }
        }
        return new Matrix(result);
    }

    public Matrix transpose() {
        int m = getRowCount(), n = getColCount();
        double[][] result = new double[n][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                result[j][i] = data[i][j];
            }
        }
        return new Matrix(result);
    }

}
