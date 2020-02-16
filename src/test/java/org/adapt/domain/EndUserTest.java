package org.adapt.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.adapt.web.rest.TestUtil;

public class EndUserTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EndUser.class);
        EndUser endUser1 = new EndUser();
        endUser1.setId(1L);
        EndUser endUser2 = new EndUser();
        endUser2.setId(endUser1.getId());
        assertThat(endUser1).isEqualTo(endUser2);
        endUser2.setId(2L);
        assertThat(endUser1).isNotEqualTo(endUser2);
        endUser1.setId(null);
        assertThat(endUser1).isNotEqualTo(endUser2);
    }
}
