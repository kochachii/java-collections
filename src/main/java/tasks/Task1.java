package tasks;

import common.Person;
import common.PersonService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
Задача 1
Метод на входе принимает List<Integer> id людей, ходит за ними в сервис
(он выдает несортированный Set<Person>, внутренняя работа сервиса неизвестна)
нужно их отсортировать в том же порядке, что и переданные id.

O(N), т.к. просто заполняем мапу и проходимся по айдишникам
 */
public class Task1 {

  private final PersonService personService;

  public Task1(PersonService personService) {
    this.personService = personService;
  }

  public List<Person> findOrderedPersons(List<Integer> personIds) {
    Map<Integer, Person> personById = personService.findPersons(personIds).stream()
        .collect(Collectors.toMap(
            Person::id,
            Function.identity(),
            (existing, current) -> existing
        ));
    return personIds.stream()
        .map(personById::get)
        .toList();
  }
}
