package algo.demo;

import algo.linear.LinearRegression;
import algo.linear.LogisticRegression;
import algo.Utils;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class App{
    private static void classificationTask(Scanner scanner) throws Exception {
        System.out.println("分类任务");
        System.out.println("请输入数字选择算法：" +
                "1: LogisticRegression");
        int k = scanner.nextInt();
        switch (k) {
            case 1: {
                LogisticRegression logisticRegression = new LogisticRegression(1e-3, 2000000,true, false);
                Utils.readClassificationData("iris", 2);
                Utils.splitTrainAndTest(0.2, true);
                double[][] xTrain = Utils.getxTrain();
                double[][] xEval = Utils.getxEval();
                double[] yTrain2 = Utils.getyTrain();
                double[] yEval2 = Utils.getyEval();
                int [] yTrain = new int[yTrain2.length];
                int [] yEval = new int[yEval2.length];
                for (int i = 0; i < yTrain.length; i++) {
                    yTrain[i] = (int)yTrain2[i];
                }
                for (int i = 0; i < yEval2.length; i++) {
                    yEval[i] = (int) yEval2[i];
                }
                logisticRegression.fit(xTrain, yTrain);
                System.out.println(String.format("Logistic Regression score on eval set: %.5f", logisticRegression.score(xTrain, yTrain)));
                System.out.println(String.format("Logistic Regression score on eval set: %.5f", logisticRegression.score(xEval, yEval)));
                break;
            }
            default: break;
        }
    }

    private static void regressionTask(Scanner scanner) throws FileNotFoundException {
        System.out.println("回归任务");
        System.out.println("请输入数字选择算法：" +
                "1: LinearRegression");
        int k = scanner.nextInt();
        switch (k) {
            case 1: {
                // LinearRegression
                Utils.readRegressionData("boston");
                Utils.splitTrainAndTest(0.2, false);
                double[][] xTrain = Utils.getxTrain();
                double[][] xEval = Utils.getxEval();
                double[] yTrain = Utils.getyTrain();
                double[] yEval = Utils.getyEval();
                LinearRegression linearRegression = new LinearRegression(1e-4, 10000, true, true, true);
                linearRegression.fit(xTrain, yTrain);
                System.out.println(String.format("score on train set: %.5f", linearRegression.score(xTrain, yTrain)));
                System.out.println(String.format("score on eval set: %.5f", linearRegression.score(xEval, yEval)));
                break;
            }
            default:break;
        }
    }

    private static void decomposionTask(Scanner scanner) {
        System.out.println("降维任务");
        System.out.println("请输入数字选择算法：" +
                "1: LogisticRegression");
        int k = scanner.nextInt();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("请输入任务类型： 1: 分类，2： 回归， 3： 降维");
        Scanner scanner = new Scanner(System.in);
        int k = scanner.nextInt();
        switch (k) {
            case 1: {
                classificationTask(scanner);
                break;
            }
            case 2: {
                regressionTask(scanner);
                break;
            }
            case 3: {
                decomposionTask(scanner);
            }
            default: break;
        }
    }
}