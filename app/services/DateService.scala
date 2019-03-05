package services

import com.google.inject._
import java.time.{Clock, LocalDate}

@ImplementedBy(classOf[RealDateService])
trait DateService {
  def now: LocalDate
}

class RealDateService @Inject()(clock: Clock) extends DateService {

   def now: LocalDate = LocalDate.now(clock)
}