package services

import com.google.inject._
import java.time.LocalDate

@ImplementedBy(classOf[RealDateService])
trait DateService {
  def now: LocalDate
}

class RealDateService @Inject()() extends DateService {

   def now: LocalDate = LocalDate.now()
}