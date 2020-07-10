package example;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;

public class ListRankedPods {
    public static void main(String[] args) {
        try {
            // Initialize the Kubernetes client library
            KubernetesClient client = new DefaultKubernetesClient();

            // Get pods with label app and with the value ranked
            PodList list = client.pods().inNamespace("minecraft").withLabel("app", "ranked").list();
            
            for (Pod item : list.getItems()) {
                // Getting the pod name
                System.out.println(item.getMetadata().getName());
            }

            // Close the kubernetes client (we can't reuse it after that) - Optional because the library close it itself
            client.close();
        } catch (KubernetesClientException exception) {
            System.out.println(exception);
        }
    }
}
