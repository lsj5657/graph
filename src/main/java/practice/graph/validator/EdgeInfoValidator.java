package practice.graph.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import practice.graph.dto.GraphDTO;

public class EdgeInfoValidator implements ConstraintValidator<ValidEdgeInfo, GraphDTO> {

    @Override
    public void initialize(ValidEdgeInfo constraintAnnotation) {
    }

    @Override
    public boolean isValid(GraphDTO graphDTO, ConstraintValidatorContext context) {
        String edgeInfo = graphDTO.getEdgeInfo();
        int expectedEdgeCount = graphDTO.getEdgeCount();

        // Treat edgeInfo as empty if it is null
        if (edgeInfo == null) {
            edgeInfo = "";
        }

        String[] lines = edgeInfo.split("\n");
        if (lines.length != expectedEdgeCount) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Edge count does not match the number of edges provided.")
                    .addPropertyNode("edgeInfo")
                    .addConstraintViolation();
            return false;
        }

        for (String line : lines) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length != 3) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Each line must contain exactly three numbers.")
                        .addPropertyNode("edgeInfo")
                        .addConstraintViolation();
                return false;
            }
            for (String part : parts) {
                try {
                    Integer.parseInt(part);
                } catch (NumberFormatException e) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("Each part must be a valid number.")
                            .addPropertyNode("edgeInfo")
                            .addConstraintViolation();
                    return false;
                }
            }
        }

        return true;
    }
}