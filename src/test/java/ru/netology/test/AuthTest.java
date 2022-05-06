package ru.netology.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataGenerator.Registration.generateUser;
import static ru.netology.data.DataGenerator.Registration.registerUser;
import static ru.netology.data.DataGenerator.generateLogin;
import static ru.netology.data.DataGenerator.generatePassword;

class AuthenticationInTestModeTest {

    @BeforeEach
    public void setup() {
        open("http://localhost:9999");
    }

    @Test
    public void successfulLoginIfUserActive() {
        var registeredUser = registerUser("active");
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $(".button").shouldHave(Condition.text("Продолжить")).click();
        $(".heading").shouldBe(Condition.text("Личный кабинет"));
    }

    @Test
    void ErrorMessageIfNotRegisteredUser() {
        var notRegisteredUser = generateUser("active");
        $("[data-test-id=login] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id=password] input").setValue(notRegisteredUser.getPassword());
        $(".button").shouldHave(Condition.text("Продолжить")).click();
        $("[data-test-id=error-notification]").shouldBe(Condition.visible).shouldHave(Condition.text("Ошибка" + " Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void ErrorMassageIfUserBlocked() {
        var blockedUser = registerUser("blocked");
        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $(".button").shouldHave(Condition.text("Продолжить")).click();
        $("[data-test-id=error-notification]").shouldBe(Condition.visible).shouldHave(Condition.text("Ошибка" + " Ошибка! Пользователь заблокирован"));
    }

    @Test
    void ErrorMassageIfWrongLogin() {
        var registeredUser = registerUser("active");
        var wrongLogin = generateLogin();
        $("[data-test-id=login] input").setValue(wrongLogin);
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $(".button").shouldHave(Condition.text("Продолжить")).click();
        $("[data-test-id=error-notification]").shouldBe(Condition.visible).shouldHave(Condition.text("Ошибка" + " Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void ErrorMassageIfWrongPassword() {
        var registeredUser = registerUser("active");
        var wrongPassword = generatePassword();
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(wrongPassword);
        $(".button").shouldHave(Condition.text("Продолжить")).click();
        $("[data-test-id=error-notification]").shouldBe(Condition.visible).shouldHave(Condition.text("Ошибка" + " Ошибка! Неверно указан логин или пароль"));
    }
}
