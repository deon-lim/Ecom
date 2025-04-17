package com.mycompany.order2.domain;

import static com.mycompany.order2.domain.Order2TestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.order2.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class Order2Test {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Order2.class);
        Order2 order21 = getOrder2Sample1();
        Order2 order22 = new Order2();
        assertThat(order21).isNotEqualTo(order22);

        order22.setId(order21.getId());
        assertThat(order21).isEqualTo(order22);

        order22 = getOrder2Sample2();
        assertThat(order21).isNotEqualTo(order22);
    }
}
