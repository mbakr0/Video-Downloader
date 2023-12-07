package online.mohmedbakr.downloadvideos.model.mapper

interface Mapper<I,O> {
    fun map(input: I): O
}
interface ListMapper<I, O>: Mapper<List<I>, List<O>>
