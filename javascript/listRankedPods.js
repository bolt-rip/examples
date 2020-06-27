const k8s = require('@kubernetes/client-node');

const kc = new k8s.KubeConfig();
kc.loadFromFile("/home/emilien/.kube/config.bolt-us-central-1"); // change the location to your kubeconfig file

const k8sApi = kc.makeApiClient(k8s.CoreV1Api);

(async () => {
    // fetch pods with the label app and as a value: ranked
    // documentation : https://kubernetes.io/docs/concepts/overview/working-with-objects/labels/
    const rankedLabel = "app=ranked";
    const rankedPodsInMinecraftNamespace = (await k8sApi.listNamespacedPod('minecraft', undefined, undefined, undefined, undefined, rankedLabel)).body;
    console.log(rankedPodsInMinecraftNamespace.items);
})();