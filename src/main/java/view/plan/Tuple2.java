package view.plan;

class Tuple2<T1, T2> {
    public T1 _1;
    public T2 _2;

    public Tuple2(T1 _1, T2 _2) {
        this._1 = _1;
        this._2 = _2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple2 e = (Tuple2)o;
        return _1.equals(e._1) && _2.equals(e._2);
    }

    @Override
    public int hashCode() {
        return _1.hashCode()*100000 + _2.hashCode();
    }
}