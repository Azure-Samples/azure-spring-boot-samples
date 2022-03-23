package org.springframework.samples.petclinic.customers;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.customers.model.Owner;
import org.springframework.samples.petclinic.customers.model.OwnerRepository;
import org.springframework.samples.petclinic.customers.model.Pet;
import org.springframework.samples.petclinic.customers.model.PetRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

@Component
public class PopulateSeedData {

  private static final Logger logger = LoggerFactory.getLogger(PopulateSeedData.class);

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private PetRepository petRepository;


  @PostConstruct
  public void populateSeedData() {

    final Pet pet1 = new Pet("1", "Leo", parseDate("2000-09-07"), "cat");
    final Pet pet2 = new Pet("2", "Basil", parseDate("2002-08-06"), "hamster");
    final Pet pet3 = new Pet("3", "Rosy", parseDate("2001-04-17"), "dog");
    final Pet pet4 = new Pet("4", "Jewel", parseDate("2000-03-07"), "dog");
    final Pet pet5 = new Pet("5", "Iggy", parseDate("2000-11-30"), "lizard");
    final Pet pet6 = new Pet("6", "George", parseDate("2000-01-20"), "snake");
    final Pet pet7 = new Pet("7", "Samantha", parseDate("1995-09-04"), "cat");
    final Pet pet8 = new Pet("8", "Max", parseDate("1995-09-04"), "cat");
    final Pet pet9 = new Pet("9", "Lucky", parseDate("1999-08-06"), "bird");
    final Pet pet10 = new Pet("10", "Mulligan", parseDate("1997-02-24"), "dog");
    final Pet pet11 = new Pet("11", "Freddy", parseDate("2000-03-09"), "bird");
    final Pet pet12 = new Pet("12", "Lucky", parseDate("2000-06-24"), "dog");
    final Pet pet13 = new Pet("13", "Sly", parseDate("2002-06-08"), "cat");

    final Owner owner1 =
        new Owner(
            "1",
            "George",
            "Franklin",
            "110 W. Liberty St.",
            "Madison",
            "6085551023",
            new HashSet<Pet>() {
              {
                add(pet1);
              }
            });
    final Owner owner2 =
        new Owner(
            "2",
            "Betty",
            "Davis",
            "638 Cardinal Ave.",
            "Sun Prairie",
            "6085551749",
            new HashSet<Pet>() {
              {
                add(pet2);
              }
            });
    final Owner owner3 =
        new Owner(
            "3",
            "Eduardo",
            "Rodriquez",
            "2693 Commerce St.",
            "McFarland",
            "6085558763",
            new HashSet<Pet>() {
              {
                add(pet3);
                add(pet4);
              }
            });
    final Owner owner4 =
        new Owner(
            "4",
            "Harold",
            "Davis",
            "563 Friendly St.",
            "Windsor",
            "6085553198",
            new HashSet<Pet>() {
              {
                add(pet5);
              }
            });
    final Owner owner5 =
        new Owner(
            "5",
            "Peter",
            "McTavish",
            "2387 S. Fair Way",
            "Madison",
            "6085552765",
            new HashSet<Pet>() {
              {
                add(pet6);
              }
            });
    final Owner owner6 =
        new Owner(
            "6",
            "Jean",
            "Coleman",
            "105 N. Lake St.",
            "Monona",
            "6085552654",
            new HashSet<Pet>() {
              {
                add(pet7);
                add(pet8);
              }
            });
    final Owner owner7 =
        new Owner(
            "7",
            "Jeff",
            "Black",
            "1450 Oak Blvd.",
            "Monona",
            "6085555387",
            new HashSet<Pet>() {
              {
                add(pet9);
              }
            });
    final Owner owner8 =
        new Owner(
            "8",
            "Maria",
            "Escobito",
            "345 Maple St.",
            "Madison",
            "6085557683",
            new HashSet<Pet>() {
              {
                add(pet10);
              }
            });
    final Owner owner9 =
        new Owner(
            "9",
            "David",
            "Schroeder",
            "2749 Blackhawk Trail",
            "Madison",
            "6085559435",
            new HashSet<Pet>() {
              {
                add(pet11);
              }
            });
    final Owner owner10 =
        new Owner(
            "10",
            "Carlos",
            "Estaban",
            "2335 Independence La.",
            "Waunakee",
            "6085555487",
            new HashSet<Pet>() {
              {
                add(pet12);
                add(pet13);
              }
            });

    this.ownerRepository.saveAll(
        Lists.newArrayList(
            owner1, owner2, owner3, owner4, owner5, owner6, owner7, owner8, owner9, owner10));
    this.petRepository.saveAll(
        Lists.newArrayList(
            pet1, pet2, pet3, pet4, pet5, pet6, pet7, pet8, pet9, pet10, pet11, pet12, pet13));
  }

  /**
   * parseDate
   * @param date
   * @return parsedDate
   */
  public static Date parseDate(String date) {
    try {
      return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    } catch (ParseException e) {
      return null;
    }
  }
}
