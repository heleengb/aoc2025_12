# Reto 12

**Arquitectura, estilo MVC (Model-View-Controller) con Patrón Command:**
El proyecto está diseñado para resolver un problema de optimización combinatoria. En el paquete **model**, `ForestZone` define el contenedor y los requisitos, `TimberShape` modela las piezas geométricas con sus rotaciones pre-calculadas, y `ZoneSolver` encapsula el algoritmo de resolución (Backtracking). En el paquete **view**, `SolutionDisplay` se encarga de mostrar los resultados. En **controller**, `FarmController` organiza el proceso: parsea la entrada compleja y distribuye el trabajo de resolución mediante comandos paralelos.

**Principios aplicados:**
* **Responsabilidad Única (SRP):** Alta cohesión. `FileSourceReader` solo lee archivos, `TimberShape` solo gestiona la geometría de la pieza, y `ValidateZoneCommand` une una zona con sus piezas para ejecutar la validación.
* **Inversión de Dependencias (DIP):** El sistema depende de abstracciones. El controlador utiliza la interfaz `FarmOperation`, lo que permitiría introducir diferentes tipos de cálculos sin modificar el flujo de procesamiento paralelo.
* **Abierto-Cerrado (OCP):** El diseño es extensible. La clase `TimberShape` es inmutable y agnóstica al tablero; si se cambiaran las reglas de colocación, solo habría que modificar `ZoneSolver` o crear un nuevo comando, sin tocar las entidades del modelo.

**Extras:**
* **Algoritmo de Backtracking:** Implementado en `ZoneSolver` para encontrar soluciones mediante prueba y error recursiva.
* **Optimización y Thread-Safety:** `TimberShape` pre-calcula todas sus variaciones (rotaciones y volteos) en el constructor. Al ser inmutable, permite que múltiples hilos accedan a las mismas formas sin condiciones de carrera.
* **Streams Paralelos:** Uso de `.parallelStream()` en el controlador para resolver múltiples zonas independientes simultáneamente, aprovechando todos los núcleos de la CPU.
* **Heurística de Ordenamiento:** El solver ordena las piezas por área descendente para podar el árbol de búsqueda lo antes posible.
