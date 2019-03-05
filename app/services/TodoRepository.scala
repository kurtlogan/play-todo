package services

import java.util.UUID

import com.google.inject.{ImplementedBy, Inject}
import models.Todo

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}

@ImplementedBy(classOf[InMemoryTodoRepository])
trait TodoRepository {

  def insert(todo: Todo): Future[Unit]

  def get(id: UUID): Future[Option[Todo]]

  def getAll: Future[List[Todo]]

  def modify(id: UUID, f: Todo => Todo): Future[Option[Todo]]
}

class InMemoryTodoRepository @Inject()(implicit ec: ExecutionContext) extends TodoRepository {

  private val store: mutable.Map[UUID, Todo] = mutable.Map[UUID, Todo]()

  override def insert(todo: Todo): Future[Unit] =
    Future.successful(store += (todo.id -> todo)).map(_ => ())

  override def get(id: UUID): Future[Option[Todo]] =
    Future.successful(store.get(id))

  override def getAll: Future[List[Todo]] =
    Future.successful(store.toList.map(_._2))

  override def modify(id: UUID, f: Todo => Todo): Future[Option[Todo]] =
    get(id).map(_.map(f)).map(t => t.map(insert).flatMap(_ => t))

}