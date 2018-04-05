
import java.util.Objects;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author moham
 */
public class ConfigPlateau {
    Position pousseurPosition;
    Position sacPosition;
    
    ConfigPlateau prec;

    public ConfigPlateau(Position pousseurPosition, Position sacPosition, ConfigPlateau prec) {
        this.pousseurPosition = pousseurPosition;
        this.sacPosition = sacPosition;
        this.prec = prec;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.pousseurPosition);
        hash = 37 * hash + Objects.hashCode(this.sacPosition);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ConfigPlateau other = (ConfigPlateau) obj;
        if (!Objects.equals(this.pousseurPosition, other.pousseurPosition)) {
            return false;
        }
        if (!Objects.equals(this.sacPosition, other.sacPosition)) {
            return false;
        }
        return true;
    }
}
