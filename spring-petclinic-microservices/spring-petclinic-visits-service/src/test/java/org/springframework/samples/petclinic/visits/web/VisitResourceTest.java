package org.springframework.samples.petclinic.visits.web;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.visits.model.Visit;
import org.springframework.samples.petclinic.visits.model.VisitRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;
import static org.springframework.samples.petclinic.visits.model.Visit.visit;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@OverrideAutoConfiguration(enabled = true)
@ActiveProfiles("test")
public class VisitResourceTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    VisitRepository visitRepository;

  @Test
  void shouldFetchVisits() throws Exception {

        List<Visit> list = Arrays.asList(visit().id("1").petId("111").build(),
            visit().id("2").petId("222").build()
        );
    when(visitRepository.findAllById(asList("111", "222"))).thenReturn(list);

    mvc.perform(get("/pets/visits?petId=111,222"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.items[0].id").value("1"))
        .andExpect(jsonPath("$.items[1].id").value("2"))
        .andExpect(jsonPath("$.items[0].petId").value("111"))
        .andExpect(jsonPath("$.items[1].petId").value("222"));
  }
}
