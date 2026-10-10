package com.graphics;

public class clase5 extends clase4 {
    class VehiculoNPC {
        float x, z;
        float angulo;
        float velocidad = 5.0f; // Velocidad constante
        float r, g, b; // Color de la carrocería

        public VehiculoNPC(float x, float z, float angulo, float r, float g, float b) {
            this.x = x;
            this.z = z;
            this.angulo = angulo;
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }
    private VehiculoNPC[] trafico = {
        new VehiculoNPC(-30, 10, 0, 0.2f, 0.8f, 0.2f),       // Auto verde
        new VehiculoNPC(10, -30, (float)Math.PI / 2, 0.8f, 0.8f, 0.2f), // Auto amarillo
        new VehiculoNPC(30, 30, (float)Math.PI, 0.2f, 0.2f, 0.8f)      // Auto azul
    };

    protected void piezaGenerica(float centroX, float centroZ, float orientacion, float x, float y, float z, float sx, float sy, float sz, float r, float g, float b) {
    float coseno = (float) Math.cos(orientacion);
    float seno = (float) Math.sin(orientacion);
    float mundoX = centroX + coseno * x + seno * z;
    float mundoZ = centroZ - seno * x + coseno * z;
    cajaGirada(mundoX, y, mundoZ, sx, sy, sz, r, g, b, orientacion);
    }

    protected void dibujarModeloAuto(float posX, float posZ, float rotacion, float r, float g, float b) {
    piezaGenerica(posX, posZ, rotacion, 0, 0.65f, 0, 1.65f, 0.55f, 2.6f, r, g, b); // auto con color personalizado
    piezaGenerica(posX, posZ, rotacion, 0, 1.12f, 0.12f, 1.3f, 0.55f, 1.25f, 0.22f, 0.65f, 0.78f); // vidrio
    
    // Ruedas
    float[] ladosRuedas = {-0.88f, 0.88f};
    float[] ejesRuedas = {-0.82f, 0.82f};
    for (float rx : ladosRuedas) {
        for (float rz : ejesRuedas) {
            piezaGenerica(posX, posZ, rotacion, rx, 0.38f, rz, 0.24f, 0.58f, 0.6f, 0.055f, 0.065f, 0.08f);
        }
    }
    // Faros
    float[] ladosFaros = {-0.55f, 0.55f};
    for (float fx : ladosFaros) {
        piezaGenerica(posX, posZ, rotacion, fx, 0.68f, -1.32f, 0.38f, 0.2f, 0.07f, 1, 0.95f, 0.65f); // Delanteros
        piezaGenerica(posX, posZ, rotacion, fx, 0.68f, 1.32f, 0.35f, 0.17f, 0.07f, 0.85f, 0.05f, 0.05f); // Traseros
    }
}
    @Override
    protected void escena() {
        super.escena(); // Dibuja la ciudad y el jugador principal
        for (VehiculoNPC npc : trafico) {
            dibujarModeloAuto(npc.x, npc.z, npc.angulo, npc.r, npc.g, npc.b);
        }
    }

    @Override
protected void actualizar(float deltaTime) {
    super.actualizar(deltaTime); // Actualiza al jugador principal

    for (VehiculoNPC npc : trafico) {
        float frenteX = -(float) Math.sin(npc.angulo);
        float frenteZ = -(float) Math.cos(npc.angulo);
        
        float siguienteX = npc.x + frenteX * npc.velocidad * deltaTime;
        float siguienteZ = npc.z + frenteZ * npc.velocidad * deltaTime;

        // Utilizamos la misma validación de colisiones del jugador
        if (puedeCircular(siguienteX, siguienteZ)) {
            npc.x = siguienteX;
            npc.z = siguienteZ;
        } else {
            // Si va a chocar contra un edificio o límite de mapa, lo obligamos a girar 90 grados
            // Selecciona aleatoriamente si girar a la izquierda o derecha
            float giro = (Math.random() > 0.5) ? (float)Math.PI / 2 : -(float)Math.PI / 2;
            
            // Alineamos el ángulo a múltiplos exactos de 90 grados (PI/2) para mantenerlos ortogonales a la calle
            npc.angulo += giro; 
            
            // Centramos un poco el vehículo en la calle actual para evitar que roce constantemente las esquinas al girar
            npc.x = Math.round(npc.x / 10.0f) * 10.0f; 
            npc.z = Math.round(npc.z / 10.0f) * 10.0f;
        }
    }
    }

    public static void main(String[] args) {
        clase5 aplicacion = new clase5(); // Crea la versión con los vehículos adicionales.
        aplicacion.run(); // Inicia el ciclo de vida completo de la aplicación.
    }
}
