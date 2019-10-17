package algo.demo;

import algo.linear.LogisticRegression;
import algo.Utils;

import java.util.Scanner;

public class App{
    private static void classificationTask(Scanner scanner) throws Exception {
        System.out.println("分类任务");
        System.out.println("请输入数字选择算法：" +
                "1: LogisticRegression");
        int k = scanner.nextInt();
        switch (k) {
            case 1: {
                LogisticRegression logisticRegression = new LogisticRegression(1e-3, 20000,true, false);
                Utils.readClassificationData("iris", 2);
                Utils.splitTrainAndTest(0.2, true);
                double[][] xTrain = Utils.getxTrain();
                double[][] xEval = Utils.getxEval();
                int[] yTrain = Utils.getyTrain();
                int[] yEval = Utils.getyEval();
                logisticRegression.fit(xTrain, yTrain);
                System.out.println(String.format("Logistic Regression score on eval set: %.5f", logisticRegression.score(xTrain, yTrain)));
                System.out.println(String.format("Logistic Regression score on eval set: %.5f", logisticRegression.score(xEval, yEval)));
                break;
            }
            default: break;
        }
    }

    private static void regressionTask(Scanner scanner) {
        System.out.println("回归任务");
        System.out.println("请输入数字选择算法：" +
                "1: LogisticRegression");
        int k = scanner.nextInt();
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