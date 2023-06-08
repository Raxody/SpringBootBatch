package com.batch.step;

import com.batch.entities.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ItemProcessorStep implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        log.info("--------> Inicio del paso de PROCESAMIENTO <--------- ");

        List<Person> people = (List<Person>) chunkContext.getStepContext()
                                          .getStepExecution()
                                          .getJobExecution()
                                          .getExecutionContext()
                                          .get("people");

        List<Person> finalPeople =people.stream().map(person -> {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/mm/yyyy HH:mm:ss");
            person.setInsertionDate(dateTimeFormatter.format(LocalDateTime.now()));
            return person;
        }).collect(Collectors.toList());

        chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .put("finalPeople", finalPeople);

        log.info("--------> Fin del paso de PROCESAMIENTO <--------- ");
        return RepeatStatus.FINISHED;
    }

}
