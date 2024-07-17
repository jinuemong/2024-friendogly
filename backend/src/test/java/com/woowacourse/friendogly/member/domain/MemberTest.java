package com.woowacourse.friendogly.member.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class MemberTest {

    @DisplayName("생성 테스트")
    @Test
    void create() {
        assertThatCode(() -> Member.builder()
                .name("누누")
                .email("crew@wooteco.com")
                .build()
        )
                .doesNotThrowAnyException();
    }

    @DisplayName("이메일 형식이 잘못되면 예외가 발생한다.")
    @NullAndEmptySource
    @ValueSource(strings = {"www@.gmail", "@gmail.com"})
    @ParameterizedTest
    void create_Fail_IllegalEmailFormat(String emailInput) {
        assertThatThrownBy(() -> Member.builder()
                .name("누누")
                .email(emailInput)
                .build()
        )
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이름 형식이 잘못되면 예외가 발생한다.")
    @NullAndEmptySource
    @ParameterizedTest
    void create_Fail_IllegalNameFormat(String nameInput) {
        assertThatThrownBy(() -> Member.builder()
                .name(nameInput)
                .email("crew@wooteco.com")
                .build()
        )
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이름이 1글자 미만 15글자 초과면 예외가 발생한다.")
    @ValueSource(strings = {"", "0000_0000_0000_1"})
    @ParameterizedTest
    void create_Fail_IllegalNameLength(String nameInput) {
        assertThatThrownBy(() -> Member.builder()
                .name(nameInput)
                .email("crew@wooteco.com")
                .build()
        )
                .isInstanceOf(IllegalArgumentException.class);
    }

}
