package semisplay;

public interface SearchTree<E extends Comparable<E>> extends Iterable<E> {

    /** Voeg de gegeven sleutel toe aan de boom als deze er nog niet in zit.
      * @return true als de sleutel effectief toegevoegd werd. */
    boolean add(E e);

    /** Zoek de gegeven sleutel op in de boom.
      * @return true als de sleutel gevonden werd. */
    boolean contains(E e);

    /** Verwijder de gegeven sleutel uit de boom.
      * @return true als de sleutel gevonden en verwijderd werd. */
    boolean remove(E e);

    /** @return het aantal sleutels in de boom. */
    int size();

    /** @return de diepte van de boom. */
    int depth();

}
