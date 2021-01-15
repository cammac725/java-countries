package local.cammac.countries.controllers;

import local.cammac.countries.models.Country;
import local.cammac.countries.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
public class CountryController {

    @Autowired
    private CountryRepository countryrepos;

    private List<Country> findCountries(List<Country> myList, CheckCountry tester) {
        List<Country> tempList = new ArrayList<>();
        for (Country c : myList) {
            if (tester.test(c)) {
                tempList.add(c);
            }
        }
        return tempList;
    }

    // http://localhost:2021/names/all
    @GetMapping(value = "/names/all", produces = {"application/json"})
    public ResponseEntity<?> listAllCountries() {
        List<Country> myList = new ArrayList<>();

        countryrepos.findAll().iterator().forEachRemaining(myList::add);
        myList.sort((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()));

        for (Country c : myList) {
            System.out.println(c);
        }

        return new ResponseEntity<>(myList, HttpStatus.OK);
    }

    // http://localhost:2021/names/start/u
    @GetMapping(value = "/names/start/{letter}", produces = {"application/json"})
    public ResponseEntity<?> listAllByName(@PathVariable char letter) {
        List<Country> myList = new ArrayList<>();
        countryrepos.findAll().iterator().forEachRemaining(myList::add);

        List<Country> rtnList = findCountries(myList, c -> c.getName().charAt(0) == letter);

        return new ResponseEntity<>(rtnList, HttpStatus.OK);
    }

    // http://localhost:2021/population/total
    @GetMapping(value = "/population/total", produces = {"application/json"})
    public ResponseEntity<?> displayPopulation() {
        List<Country> myList = new ArrayList<>();

        countryrepos.findAll().iterator().forEachRemaining(myList::add);

        long total = 0;
        for (Country c : myList) {
            total = total + c.getPopulation();
        }

        System.out.println("Population total: " + total);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // http://localhost:2021/population/min
    @GetMapping(value = "/population/min", produces = {"application/json"})
    public ResponseEntity<?> findMinPopulation() {
        List<Country> myList = new ArrayList<>();

        countryrepos.findAll().iterator().forEachRemaining(myList::add);
        myList.sort((c1, c2) -> (int) (c1.getPopulation() - c2.getPopulation()));

        Country minPop = myList.get(0);
        System.out.println(minPop);

        return new ResponseEntity<>(minPop,HttpStatus.OK);
    }

    // http://localhost:2021/population/max
    @GetMapping(value = "/population/max", produces = {"application/json"})
    public ResponseEntity<?> findMaxPopulation() {
        List<Country> myList = new ArrayList<>();

        countryrepos.findAll().iterator().forEachRemaining(myList::add);
        myList.sort((c1, c2) -> (int) (c2.getPopulation() - c1.getPopulation()));

        Country maxPop = myList.get(0);
        System.out.println(maxPop);

        return new ResponseEntity<>(maxPop, HttpStatus.OK);
    }

    // http://localhost:2021/population/median
    @GetMapping(value = "/population/median", produces = {"application/json"})
    public ResponseEntity<?> findMedianPopulation() {
        List<Country> myList = new ArrayList<>();

        countryrepos.findAll().iterator().forEachRemaining(myList::add);
        myList.sort(Comparator.comparing(Country::getPopulation));
        int median = myList.size() / 2 + 1;

        return new ResponseEntity<>(myList.get(median), HttpStatus.OK);
    }
}