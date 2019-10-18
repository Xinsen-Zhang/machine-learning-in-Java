package algo.utilities;

public class Vector {
    private double[] data;
    public Vector(double[] data) {
        this.data = data;
    }

    public Vector(Vector vector) {
        this.data = vector.getData();
    }

    public double[] getData() {
        return data;
    }

    public double getVectorL2Norm() {
        double norm = 0.0;
        int length = data.length;
        for (int i = 0; i < length; i++) {
            norm += (data[i] * data[i]);
        }
        return Math.sqrt(norm);
    }

    public int getLength() {
        return data.length;
    }

    public double dot(Vector vector) throws Exception {
        if (getLength() != vector.getLength()) {
            throw new Exception("向量长度不一致");
        }
        int length = getLength();
        double[] data2 = vector.getData();
        double product = 0.0;
        for (int i = 0; i < length; i++) {
            product += (data[i] * data2[i]);
        }
        return product;
    }

    public void divide(double num) {
        int length = getLength();
        for (int i = 0; i < length; i++) {
            data[i] /= num;
        }
    }
}
