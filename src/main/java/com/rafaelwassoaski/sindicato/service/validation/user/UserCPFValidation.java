package com.rafaelwassoaski.sindicato.service.validation.user;

import com.rafaelwassoaski.sindicato.entity.CustomUser;
import com.rafaelwassoaski.sindicato.exceptions.BaseException;
import com.rafaelwassoaski.sindicato.exceptions.EmailAlreadyInUseException;
import com.rafaelwassoaski.sindicato.exceptions.InvalidCPFException;
import com.rafaelwassoaski.sindicato.repository.UserRepository;
import com.rafaelwassoaski.sindicato.service.validation.ChainValidation;

import java.util.InputMismatchException;
import java.util.Optional;

public class UserCPFValidation extends ChainValidation<CustomUser> {

    private final int WEIGHT_DIGIT_10 = 10;
    private final int WEIGHT_DIGIT_11 = 11;

    public UserCPFValidation(ChainValidation nextValidation) {
        super(nextValidation);
    }

    @Override
    public boolean isValid(CustomUser objectToValidate) throws BaseException {
        if (!isCPFValid(objectToValidate.getCpf())) {
            throw new InvalidCPFException();
        }

        return this.callNext(objectToValidate);
    }

    private boolean isCPFValid(String cpf) {
        cpf = cpf.replaceAll("\\D", "");

        if (this.isCpfFake(cpf) || cpf.length() != 11) {
            return false;
        }

        try {
            char digit10 = this.calculateDigit(cpf, WEIGHT_DIGIT_10);
            char digit11 = this.calculateDigit(cpf, WEIGHT_DIGIT_11);

            return (digit10 == cpf.charAt(9)) && (digit11 == cpf.charAt(10));
        } catch (InputMismatchException erro) {
            return false;
        }
    }

    private boolean isCpfFake(String cpf) {
        return cpf.equals("00000000000") ||
                cpf.equals("11111111111") ||
                cpf.equals("22222222222") || cpf.equals("33333333333") ||
                cpf.equals("44444444444") || cpf.equals("55555555555") ||
                cpf.equals("66666666666") || cpf.equals("77777777777") ||
                cpf.equals("88888888888") || cpf.equals("99999999999");
    }

    private char calculateDigit(String cpf, int weight) {
        int sum = this.sumCpfDigits(cpf, weight);
        return this.determineDigit(sum);
    }

    private char determineDigit(int sum) {
        int divisionMod = 11 - (sum % 11);
        if ((divisionMod == 10) || (divisionMod == 11)) {
            return '0';
        }

        return (char) (divisionMod + 48);
    }

    private int sumCpfDigits(String cpf, int weight){
        int sum = 0;
        int number;
        int cpfLenght = weight - 1;

        for (int index = 0; index < cpfLenght; index++) {
            number = (cpf.charAt(index) - 48);
            sum = sum + (number * weight);
            weight = weight - 1;
        }

        return sum;
    }
}
