
package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tools.FactoryPostgres;

public class ConversaDAO {
    private Connection c;
    
    /*
        Por não haver necessidade da conversa enquanto modelo no código, 
        todos os métodos que deveriam retorná-la, retornaram seu id
    */
    
    public ConversaDAO() {
        this.c = FactoryPostgres.getConexaoPostgres();
    }
    
    public int select(int id_remetente, int id_destinatario) {
        String sql = "SELECT id FROM arthur_ribeiro.conversa WHERE cliente1 = ? AND cliente2 = ? OR cliente1 = ? AND cliente2 = ?";
        
        try(PreparedStatement trans = c.prepareStatement(sql)) {
            trans.setInt(1, id_remetente);
            trans.setInt(4, id_remetente);
            trans.setInt(2, id_destinatario);
            trans.setInt(3, id_destinatario);
            
            ResultSet resultado = trans.executeQuery();
            
            if(resultado.next()) {
                return resultado.getInt("id");
            }else {
                return insert(id_remetente, id_destinatario);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            Logger.getLogger(ConversaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    public int insert(int id_remetente, int id_destinatario) {
        String sql = "INSERT INTO arthur_ribeiro.conversa(cliente1,cliente2) VALUES (?,?) returning id";
        
        try(PreparedStatement trans = c.prepareStatement(sql)){
            trans.setInt(1, id_remetente);
            trans.setInt(2, id_destinatario);
            
            ResultSet result = trans.executeQuery();
            
            if(result.next()) {
                return result.getInt("id");
            }else {
                System.err.println("Nao foi retornado nada");
                return 0;
            }
            
        } catch (SQLException ex) {
            
            System.out.println(ex.getMessage());
            return 0;
        }
    }
}
