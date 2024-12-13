package tasks;

import common.Person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
Далее вы увидите код, который специально написан максимально плохо.
Постарайтесь без ругани привести его в надлежащий вид
P.S. Код в целом рабочий (не везде), комментарии оставлены чтобы вам проще понять чего же хотел автор
P.P.S Здесь ваши правки необходимо прокомментировать (можно в коде, можно в PR на Github)
 */
public class Task9 {

  private long count;

  // Костыль, эластик всегда выдает в топе "фальшивую персону".
  // Конвертируем начиная со второй

  // Замена на скип позволяет избегать изменение списка, которое может привести к неожиданному поведению
  public List<String> getNames(List<Person> persons) {
    return persons.stream()
        .skip(1)
        .map(Person::firstName)
        .collect(Collectors.toList());
  }

  // Зачем-то нужны различные имена этих же персон (без учета фальшивой разумеется)

  // Удаление дистинкта позволяет избавиться от излишнего метода + просто более лаконичная запись
  public Set<String> getDifferentNames(List<Person> persons) {
    return new HashSet<>(getNames(persons));
  }

  // Тут фронтовая логика, делаем за них работу - склеиваем ФИО

  // Исправлена очепятка (было second вместо middle)
  // Преобразование в стрим помогает создать "нераздельный логически блок", улучшить масштабируемость
  public String convertPersonToString(Person person) {
    return Stream.of(person.secondName(), person.firstName(), person.middleName())
        .filter(Objects::nonNull)
        .collect(Collectors.joining(" "))
        .trim();
  }

  // словарь id персоны -> ее имя

  // Преобразование в стрим помогает создать "нераздельный логически блок"
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
    return persons.stream()
        .collect(Collectors.toMap(
            Person::id,
            this::convertPersonToString,
            (existing, current) -> existing
        ));
  }

  // есть ли совпадающие в двух коллекциях персоны?

  // Улучшение времени до O(N)
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    Set<Person> uniquePersons1 = new HashSet<>(persons1);
    return persons2.stream()
        .anyMatch(uniquePersons1::contains);
  }

  // Посчитать число четных чисел

  // Преобразование в стрим помогает создать "нераздельный логически блок", не таскать переменную
  public long countEven(Stream<Integer> numbers) {
    return numbers
        .filter(i -> i % 2 == 0)
        .count();
  }

  // Загадка - объясните почему assert тут всегда верен
  // Пояснение в чем соль - мы перетасовали числа, обернули в HashSet, а toString() у него вернул их в сортированном порядке

  // *Псевдокод и умозаключения*
  // hashCode = Integer.hashCode(number), а также hashCode == number (1)
  // bucketIndex = hashCode % (set.capacity - 1) (2)
  // set.loadFactor = 0.75f (3)
  // Из (1), (2) и (3): что при "последовательном" добавлении n элементов, set.capacity будет больше,
  // чем последний и по совместительству наибольший элемент => значит hashCode() для любого элемента
  // выдаст уникальный bucketIndex, притом "возрастающий" порядок сохранится
  void listVsSet() {
    List<Integer> integers = IntStream.rangeClosed(1, 10000).boxed().collect(Collectors.toList());
    List<Integer> snapshot = new ArrayList<>(integers);
    Collections.shuffle(integers);
    Set<Integer> set = new HashSet<>(integers);
    assert snapshot.toString().equals(set.toString());
  }
}
