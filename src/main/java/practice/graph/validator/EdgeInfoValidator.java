package practice.graph.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import practice.graph.dto.GraphDTO;

@Slf4j
public class EdgeInfoValidator implements ConstraintValidator<ValidEdgeInfo, GraphDTO> {

    @Override
    public void initialize(ValidEdgeInfo constraintAnnotation) {
    }

    @Override
    public boolean isValid(GraphDTO graphDTO, ConstraintValidatorContext context) {
        String edgeInfo = graphDTO.getEdgeInfo();
        int expectedEdgeCount = graphDTO.getEdgeCount();





        String[] lines = edgeInfo.split("\n");

        if(edgeInfo == "" ){
            if(expectedEdgeCount==0) return true;
            else return false;
        }

        if (lines.length != expectedEdgeCount) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("간선 입력이 올바르지 않습니다.")
                    .addPropertyNode("edgeInfo")
                    .addConstraintViolation();
            return false;
        }

        for (String line : lines) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length != 3) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("간선 입력이 올바르지 않습니다.")
                        .addPropertyNode("edgeInfo")
                        .addConstraintViolation();
                return false;
            }
            for (String part : parts) {
                try {
                    Integer.parseInt(part);
                } catch (NumberFormatException e) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("간선 입력이 올바르지 않습니다.")
                            .addPropertyNode("edgeInfo")
                            .addConstraintViolation();
                    return false;
                }
            }
        }

        return true;
    }
}