const k8s = require('@kubernetes/client-node');

const kc = new k8s.KubeConfig();
kc.loadFromFile("/home/emilien/.kube/config.bolt-us-central-1"); // change the location to your kubeconfig file

const k8sApi = kc.makeApiClient(k8s.CoreV1Api);

(async () => {
    const podsInMinecraftNamespace = (await k8sApi.listNamespacedPod('minecraft')).body;
    const rankedPodsInMinecraftNamespace = podsInMinecraftNamespace.items.filter(pod => pod.metadata.labels.app === "ranked");
    console.log(rankedPodsInMinecraftNamespace);
})();