package org.springframework.samples.petclinic.visits;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.visits.model.Visit;
import org.springframework.samples.petclinic.visits.model.VisitRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class PopulateSeedData {

  private static final Logger logger = LoggerFactory.getLogger(PopulateSeedData.class);

    @Autowired
    private VisitRepository visitRepository;

  //    @PostConstruct
  //    @Order(2) // not evaluated by Spring
  //    void postConstruct(){
  //        logger.info("@PostConstruct");
  //    }

  // @Order(2) // not evaluated by Spring

  /**
   *
   * @throws ParseException
   */
  @PostConstruct
  public void populateSeedData() throws ParseException {
    final Visit visit1 = new Visit("1",
                                 "7",
                                       new SimpleDateFormat("yyyy-dd-MM").parse("2010-03-04"),
                             "rabies shot");
    final Visit visit2 = new Visit("2",
                                 "8",
                                       new SimpleDateFormat("yyyy-dd-MM").parse("2011-03-04"),
                             "rabies shot");
    final Visit visit3 = new Visit("3",
                                 "8",
                                       new SimpleDateFormat("yyyy-dd-MM").parse("2009-06-04"),
                              "neutered");
    final Visit visit4 = new Visit("4",
                                 "7",
                                       new SimpleDateFormat("yyyy-dd-MM").parse("2008-09-04"),
                              "spayed");
    this.visitRepository.saveAll(Lists.newArrayList(visit1, visit2, visit3, visit4));
  }
}
