package io.ourongbin.netty.examples.pojo;

import lombok.Data;

@Data
public class CalculateReq {
    int firstOperand;
    int secondOperand;
    char operator;

    public int getResult() {
        switch (operator) {
            case '+':
                return firstOperand + secondOperand;
            case '-':
                return firstOperand - secondOperand;
            case '*':
                return firstOperand * secondOperand;
            case '/':
                return firstOperand / secondOperand;
            default:
                throw new RuntimeException("Illegal operator");
        }
    }
}
