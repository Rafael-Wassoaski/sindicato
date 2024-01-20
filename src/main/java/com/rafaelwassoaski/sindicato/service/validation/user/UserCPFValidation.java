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

    private boolean isCPFValid(String CPF) {
        CPF = CPF.replaceAll("\\D", "");

        if (CPF.equals("00000000000") ||
                CPF.equals("11111111111") ||
                CPF.equals("22222222222") || CPF.equals("33333333333") ||
                CPF.equals("44444444444") || CPF.equals("55555555555") ||
                CPF.equals("66666666666") || CPF.equals("77777777777") ||
                CPF.equals("88888888888") || CPF.equals("99999999999") ||
                (CPF.length() != 11)) {
            return false;
        }

        char dig10, dig11;
        int sm, i, r, num, peso;

        try {
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                num = (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else dig10 = (char) (r + 48);

            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig11 = '0';
            else dig11 = (char) (r + 48);

            return (dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10));
        } catch (InputMismatchException erro) {
            return false;
        }
    }
}
