package org.springframework.samples.petclinic.vets;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.vets.model.Specialty;
import org.springframework.samples.petclinic.vets.model.Vet;
import org.springframework.samples.petclinic.vets.model.VetRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.util.HashSet;

@Component
public class PopulateSeedData {

  private static final Logger logger = LoggerFactory.getLogger(PopulateSeedData.class);

  @Autowired
  private VetRepository repository;

  /**
   * populateSeedData
   * @throws ParseException
   */
  @PostConstruct
  public void populateSeedData() throws ParseException {
    final Specialty speciality1 = new Specialty(1, "radiology");
    final Specialty speciality2 = new Specialty(2, "surgery");
    final Specialty speciality3 = new Specialty(3, "dentistry");

    final Vet vet1 = new Vet(1, "James", "Carter", null);
    final Vet vet2 =
        new Vet(
            2,
            "Helen",
            "Leary",
            new HashSet<Specialty>() {
              {
                add(speciality1);
              }
            });
    final Vet vet3 =
        new Vet(
            3,
            "Linda",
            "Douglas",
            new HashSet<Specialty>() {
              {
                add(speciality2);
                add(speciality3);
              }
            });
    final Vet vet4 =
        new Vet(
            4,
            "Rafael",
            "Ortega",
            new HashSet<Specialty>() {
              {
                add(speciality2);
              }
            });
    final Vet vet5 =
        new Vet(
            5,
            "Henry",
            "Stevens",
            new HashSet<Specialty>() {
              {
                add(speciality1);
              }
            });
    final Vet vet6 = new Vet(6, "James", "Carter", null);

    this.repository.saveAll(Lists.newArrayList(vet1, vet2, vet3, vet4, vet5, vet6));
  }
}
