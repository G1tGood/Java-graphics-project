package primitives;

/** interface for four-variables lambda function
 * @author Yoav Babayoff and Avishai Sachor */
@FunctionalInterface
public
interface FourConsumer<A,B,C,D> {
    /** runs the void function method */
    void accept(A a, B b, C c, D d);
}
